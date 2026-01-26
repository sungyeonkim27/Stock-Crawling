// csrf 토큰
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

// 공통 fetch 옵션
function csrfOptions(method) {
    return {
        method: method,
        headers: { [csrfHeader]: csrfToken }
    }
}
// ------------------------------------------

// 크롤링 요청
async function loadStocks() {
    const res = await fetch('/api/stocks');
    const data = await res.json();
    renderData(data, 'dataContainer');
}
async function loadMarket() {
    const res = await fetch('/api/market/summary');
    const data = await res.json();
    renderData(data, 'dataContainer');
}
// -------------------------------------------

// 조회 요청
async function fetchStocks() {
    const res = await fetch('/api/stocks/allSearch');
    const data = await res.json();
    renderData(data, 'dataContainer');
}

async function fetchMarket() {
    const res = await fetch('/api/market/allSearch');
    const data = await res.json();
    renderData(data, 'dataContainer');
}



async function searchCrawledPrice() {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) return alert("검색어를 입력하세요.");

    const res = await fetch(`/api/stocks/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await res.json();
    renderData(data, 'dataContainer');
}

async function searchKeywordCrawledPrice(keyword) {

    const res = await fetch(`/api/stocks/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await res.json();
    renderData(data, 'dataContainer');
}


async function searchMarketNews() {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) return alert("검색어를 입력하세요.");

    const res = await fetch(`/api/market/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await res.json();
    renderData(data, 'dataContainer');
}
// ----------------------------------------------

// 삭제 요청
async function deleteStocks() {
    const stockCode = document.getElementById("deleteCode").value;
    if (!stockCode.trim()) return alert("삭제할 종목코드를 입력하세요.");

    const res = await fetch(`/api/stocks/deleteByCode?stockCode=${encodeURIComponent(stockCode)}`, csrfOptions("DELETE"));

    if(res.ok) {
        alert("삭제 완료");
        location.reload();
    } else {
        alert("삭제 실패");
    }
}

async function deleteMarketData() {
    const res = await fetch(`/api/market/allDelete`, csrfOptions("DELETE"));
    if(res.ok) {
        alert("삭제 완료");
        location.reload();
    } else {
        alert("삭제 실패");
    }
}
// --------------------------

// 관심 종목 관련 요청
async function fetchWatchlist() {
    const res = await fetch('/api/watchlist');
    const data = await res.json();
    renderWatchlist(data, 'watchlist-container');
}

async function addWatchlist() {
    const stockCode = document.getElementById('watchlist-stockCode').value;
    if (!stockCode.trim()) {
        return alert("종목을 입력하세요");
    }
    try {
        const response = await fetch(`/api/watchlist?stockCode=${stockCode}`, csrfOptions("POST"));
        if (response.ok) {
            alert("관심 종목 추가 완료");
            fetchWatchlist();
        } else {
            alert("추가 실패");
        }
    } catch(error) {
        console.error("Error", error);
        alert("요청 중 오류 발생");
    }
}

function renderWatchlist(data, containerId) {
    const container = document.getElementById(containerId);

    if (!Array.isArray(data) || data.length === 0) {
        container.innerHTML = `<p class="empty-message">관심 종목이 없습니다.</p>`;
        return;
    }
    let html = `<div class="watchlist-cards">`;
    data.forEach(item => {
        html += `
            <div class="watchlist-card">
                <div class="watchlist-row">
                    <span class="watchlist-label">종목명 : </span>
                    <span class="watchlist-value stock-name"> ${item.stockName} </span>
                </div>
                <div class="watchlist-row">
                    <span class="watchlist-label">종목번호 : </span>
                    <span class="watchlist-value"> ${item.stockCode} </span>
                </div>
                <div class="watchlist-row">
                    <button class="watchlist-button">크롤링</button>
                    <button class="watchlist-button" onclick="searchKeywordCrawledPrice('${item.stockCode}')">조회</button>
                    <button class="watchlist-button">관심 종목 삭제</button>
                </div>
            </div>
        `;
    });
    html += `</div>`
    container.innerHTML = html;
}

// ------------------------
function renderData(data, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
}
// --------------------------------