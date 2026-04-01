import { useState } from 'react';
import { fetchStockQuote, searchStocks } from '../api/marketApi';

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <span className="stat-label">{label}</span>
      <span className="stat-value">{value ?? '—'}</span>
    </div>
  );
}

export default function StockLookup() {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [quote, setQuote] = useState(null);
  const [searching, setSearching] = useState(false);
  const [loadingQuote, setLoadingQuote] = useState(false);
  const [error, setError] = useState(null);

  async function handleSearch(e) {
    e.preventDefault();
    if (!query.trim()) return;
    setSearching(true);
    setError(null);
    setSearchResults([]);
    setQuote(null);
    try {
      const res = await searchStocks(query);
      setSearchResults(res.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setSearching(false);
    }
  }

  async function handleSelectSymbol(symbol) {
    setLoadingQuote(true);
    setError(null);
    setSearchResults([]);
    setQuery(symbol);
    try {
      const res = await fetchStockQuote(symbol);
      setQuote(res.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingQuote(false);
    }
  }

  const isPositive = quote?.change >= 0;

  return (
    <div>
      <h2>Stock Quote Lookup</h2>
      <p className="subtitle">Search by company name or symbol (e.g. AAPL, TSLA, MSFT)</p>

      <form className="search-form" onSubmit={handleSearch}>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search symbol or company…"
          className="search-input"
        />
        <button type="submit" className="search-btn" disabled={searching}>
          {searching ? 'Searching…' : 'Search'}
        </button>
      </form>

      {error && <div className="error">Error: {error}</div>}

      {searchResults.length > 0 && (
        <div className="search-results">
          <p className="results-label">Select a result to get the quote:</p>
          {searchResults.slice(0, 6).map((r) => (
            <button
              key={r.symbol}
              className="result-item"
              onClick={() => handleSelectSymbol(r.symbol)}
            >
              <span className="result-symbol">{r.symbol}</span>
              <span className="result-name">{r.name}</span>
              <span className="result-meta">{r.region} · {r.currency}</span>
            </button>
          ))}
        </div>
      )}

      {loadingQuote && <div className="loading">Fetching quote…</div>}

      {quote && (
        <div className="quote-card">
          <div className="quote-header">
            <div>
              <h3 className="quote-symbol">{quote.symbol}</h3>
              <span className="quote-date">Latest trading day: {quote.latestTradingDay}</span>
            </div>
            <div className="quote-price-block">
              <span className="quote-price">${quote.price?.toFixed(2)}</span>
              <span className="quote-change" style={{ color: isPositive ? '#16a34a' : '#dc2626' }}>
                {isPositive ? '+' : ''}{quote.change?.toFixed(2)} ({isPositive ? '+' : ''}{quote.changePercent?.toFixed(2)}%)
              </span>
            </div>
          </div>
          <div className="stats-grid">
            <StatCard label="Open" value={`$${quote.open?.toFixed(2)}`} />
            <StatCard label="Previous Close" value={`$${quote.previousClose?.toFixed(2)}`} />
            <StatCard label="Day High" value={`$${quote.high?.toFixed(2)}`} />
            <StatCard label="Day Low" value={`$${quote.low?.toFixed(2)}`} />
            <StatCard label="Volume" value={quote.volume?.toLocaleString()} />
          </div>
          <p className="source-note">Source: Alpha Vantage · Data cached 60s</p>
        </div>
      )}
    </div>
  );
}