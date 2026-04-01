import { useFetch } from '../hooks/useFetch';
import { fetchTopCryptos } from '../api/marketApi';

function PriceChange({ value }) {
  if (value == null) return <span>—</span>;
  const positive = value >= 0;
  return (
    <span style={{ color: positive ? '#16a34a' : '#dc2626', fontWeight: 600 }}>
      {positive ? '+' : ''}{value.toFixed(2)}%
    </span>
  );
}

function formatUSD(value) {
  if (value == null) return '—';
  if (value >= 1e9) return `$${(value / 1e9).toFixed(2)}B`;
  if (value >= 1e6) return `$${(value / 1e6).toFixed(2)}M`;
  return `$${value.toLocaleString()}`;
}

export default function CryptoTable() {
  const { data: coins, loading, error, refetch } = useFetch(
    () => fetchTopCryptos(10), []
  );

  if (loading) return <div className="loading">Fetching market data…</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div>
      <div className="table-header">
        <h2>Top 10 Cryptocurrencies</h2>
        <button className="refresh-btn" onClick={refetch}>↻ Refresh</button>
      </div>
      <div className="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Price (USD)</th>
              <th>24h Change</th>
              <th>Market Cap</th>
              <th>Volume (24h)</th>
              <th>24h High</th>
              <th>24h Low</th>
            </tr>
          </thead>
          <tbody>
            {coins.map((coin) => (
              <tr key={coin.id}>
                <td className="rank">{coin.marketCapRank}</td>
                <td>
                  <div className="coin-name">
                    <img src={coin.image} alt={coin.name} width={24} height={24} />
                    <div>
                      <span className="name">{coin.name}</span>
                      <span className="ticker">{coin.symbol.toUpperCase()}</span>
                    </div>
                  </div>
                </td>
                <td className="price">${coin.currentPrice?.toLocaleString()}</td>
                <td><PriceChange value={coin.priceChangePercentage24h} /></td>
                <td>{formatUSD(coin.marketCap)}</td>
                <td>{formatUSD(coin.totalVolume)}</td>
                <td>${coin.high24h?.toLocaleString()}</td>
                <td>${coin.low24h?.toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="source-note">Source: CoinGecko · Data cached 60s</p>
    </div>
  );
}