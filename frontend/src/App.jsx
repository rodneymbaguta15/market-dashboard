import { useState } from 'react'
import CryptoTable from './components/CryptoTable'
import StockLookup from './components/StockLookup'

const TABS = [
  { id: 'crypto', label: '⬡  Crypto' },
  { id: 'stocks', label: '◈  Stocks' },
]

export default function App() {
  const [activeTab, setActiveTab] = useState('crypto')

  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <h1>Live Market Dashboard</h1>
          <div className="header-sub">Powered by CoinGecko &amp; Alpha Vantage</div>
        </div>
        <div className="live-badge">
          <span className="live-dot" />
          LIVE DATA
        </div>
      </header>

      <nav className="tabs">
        {TABS.map(tab => (
          <button
            key={tab.id}
            className={`tab-btn${activeTab === tab.id ? ' active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>

      <div className="card">
        {activeTab === 'crypto' && <CryptoTable />}
        {activeTab === 'stocks' && <StockLookup />}
      </div>
    </div>
  )
}