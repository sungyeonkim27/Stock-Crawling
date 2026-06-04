// CSRF 토큰
const csrfToken  = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

function csrfOptions(method) {
    return { method, headers: { [csrfHeader]: csrfToken } };
}

let stockChart = null;
let activeCode = null;

// ===========================
// 관심 종목
// ===========================
async function fetchWatchlist() {
    const res = await fetch('/api/watchlist');
    if (!res.ok) return;
    const data = await res.json();
    renderWatchlist(data);
}

async function addWatchlist() {
    const stockCode = document.getElementById('watchlist-stockCode').value;
    if (!stockCode.trim()) return alert("종목 코드를 입력하세요.");
    try {
        const res = await fetch(`/api/watchlist?stockCode=${stockCode}`, csrfOptions("POST"));
        if (res.ok) {
            document.getElementById('watchlist-stockCode').value = '';
            fetchWatchlist();
        } else {
            alert("추가 실패. 종목 코드를 확인하세요.");
        }
    } catch (e) {
        console.error(e);
    }
}

function renderWatchlist(data) {
    const container = document.getElementById('watchlist-container');
    const tabBar    = document.getElementById('stockTabBar');

    if (!Array.isArray(data) || data.length === 0) {
        container.innerHTML = `<p class="empty-message">관심 종목이 없습니다.</p>`;
        tabBar.innerHTML    = `<span class="tab-empty">관심 종목을 추가하면 탭이 생성됩니다.</span>`;
        document.getElementById('stockPage').innerHTML =
            `<div class="stock-page-empty">종목 탭을 선택하면 차트와 데이터를 확인할 수 있습니다.</div>`;
        return;
    }

    // 사이드바 카드
    let cardHtml = `<div class="watchlist-cards">`;
    data.forEach(item => {
        cardHtml += `
            <div class="watchlist-card ${item.stockCode === activeCode ? 'active' : ''}"
                 onclick="switchStockTab('${item.stockCode}', '${item.stockName}')">
                <div class="watchlist-card-header">
                    <span class="watchlist-name">${item.stockName}</span>
                    <span class="watchlist-code">${item.stockCode}</span>
                </div>
            </div>`;
    });
    cardHtml += `</div>`;
    container.innerHTML = cardHtml;

    // 탭 바
    let tabHtml = '';
    data.forEach(item => {
        tabHtml += `
            <button class="stock-tab ${item.stockCode === activeCode ? 'active' : ''}"
                    onclick="switchStockTab('${item.stockCode}', '${item.stockName}')">
                ${item.stockName}
            </button>`;
    });
    tabBar.innerHTML = tabHtml;

    // 첫 탭 자동 선택
    if (!activeCode) {
        switchStockTab(data[0].stockCode, data[0].stockName);
    }
}

// ===========================
// 탭 전환 + 데이터 로드
// ===========================
async function switchStockTab(code, name) {
    activeCode = code;

    // 탭/카드 active 갱신
    document.querySelectorAll('.stock-tab').forEach(t =>
        t.classList.toggle('active', t.textContent.trim() === name));
    document.querySelectorAll('.watchlist-card').forEach(c => {
        const codeEl = c.querySelector('.watchlist-code');
        if (codeEl) c.classList.toggle('active', codeEl.textContent.trim() === code);
    });

    // 로딩 표시
    document.getElementById('stockPage').innerHTML =
        `<div class="stock-page-empty">데이터를 불러오는 중...</div>`;

    const res = await fetch(`/api/stocks/history?code=${code}&stockName=${encodeURIComponent(name)}`);
    if (!res.ok) {
        document.getElementById('stockPage').innerHTML =
            `<div class="stock-page-empty">데이터를 불러오지 못했습니다.</div>`;
        return;
    }
    const data = await res.json();
    renderStockPage(name, data);
}

// ===========================
// 차트 + 테이블 렌더링
// ===========================
function renderStockPage(name, data) {
    const page = document.getElementById('stockPage');
    page.innerHTML = `
        <div class="chart-section">
            <div class="chart-header">
                <span class="chart-title">${name} 종가 추이 (최근 90거래일)</span>
            </div>
            <canvas id="stockChart"></canvas>
        </div>
        <div class="table-section">
            <table class="stock-table">
                <thead>
                    <tr><th>날짜</th><th>종가</th></tr>
                </thead>
                <tbody id="stockTableBody"></tbody>
            </table>
        </div>`;

    const labels = data.map(d => d.tradeDate);
    const prices = data.map(d => d.closePrice);

    // 테이블 (최신순)
    const tbody = document.getElementById('stockTableBody');
    [...data].reverse().forEach(d => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${d.tradeDate}</td><td>₩${d.closePrice.toLocaleString()}</td>`;
        tbody.appendChild(tr);
    });

    // 차트
    if (stockChart) stockChart.destroy();
    stockChart = new Chart(document.getElementById('stockChart').getContext('2d'), {
        type: 'line',
        data: {
            labels,
            datasets: [{
                label: name,
                data: prices,
                borderColor: '#3fb950',
                backgroundColor: 'rgba(63,185,80,0.07)',
                fill: true,
                tension: 0.3,
                pointRadius: 2,
                pointBackgroundColor: '#3fb950',
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: { callbacks: { label: ctx => `₩${ctx.raw.toLocaleString()}` } }
            },
            scales: {
                x: {
                    ticks: { color: '#8b949e', maxTicksLimit: 10 },
                    grid: { color: '#21262d' }
                },
                y: {
                    ticks: { color: '#8b949e', callback: v => `₩${v.toLocaleString()}` },
                    grid: { color: '#21262d' }
                }
            }
        }
    });
}

// ===========================
// 페이지 진입 시 자동 로드
// ===========================
document.addEventListener('DOMContentLoaded', () => fetchWatchlist());
