const BASE = '/api';

async function get(path) {
  const res = await fetch(`${BASE}${path}`);
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.error || `Request failed: ${res.status}`);
  }
  return res.json();
}

// Crypto
export const fetchTopCryptos = (limit = 10) => get(`/crypto/top?limit=${limit}`);
export const fetchCryptoById = (id) => get(`/crypto/${id}`);

// Stocks
export const fetchStockQuote = (symbol) => get(`/stocks/quote/${symbol}`);
export const searchStocks = (query) => get(`/stocks/search?q=${encodeURIComponent(query)}`);