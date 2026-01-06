// csrf 토큰
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

// 공통 fetch 옵션
function csrfOptions(method) {
    return {
        method: method,
        header: { [csrfHeader]: csrfToken }
    }
}

// 크롤링 요청
async function loadStocks() {
    const res = await fetch('/api/stocks');
    const data = await res.json();
    renderData(data);
}

async function loadMarket() {
    const res = await fetch('/api/market/summary');
    const data = await res.json();
    renderData(data);
}

// 조회 요청
async function fetchStocks() {
    const res = await fetch('/api/stocks/allSearch');
    const data = await res.json();
    renderData(data);
}

async function fetchMarket() {
    const res = await fetch('/api/market/allSearch');
    const data = await res.json();
    renderData(data);
}

async function searchStocks() {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) return alert("검색어를 입력하세요.");

    const res = await fetch(`/api/stocks/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await res.json();
    renderData(data);
}


async function searchMarketNews() {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) return alert("검색어를 입력하세요.");

    const res = await fetch(`/api/market/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await res.json();
    renderData(data);
}

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

function renderData(data) {
    const container = document.getElementById('dataContainer');
    container.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
}