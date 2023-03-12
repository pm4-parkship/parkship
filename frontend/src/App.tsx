import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';

function App() {


  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);


  useEffect(() => {
    setLoading(true);

    fetch('/api/dadjokes')
        .then(response => response.text())
        .then(data => {
          console.log(data)
          setMessage(data)
          setLoading(false);
        })
  }, []);

  if (loading) {
    return <p>Loading...</p>;
  }
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <h3 className="App-title">{message}</h3>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
