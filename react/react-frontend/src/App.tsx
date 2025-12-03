import { useState } from 'react';
import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import NavHeader from './components/NavHeader';

export type Screen = "home" | "login" | "register" | "profile";

function App() {
  const [screen, setScreen] = useState<Screen>("home");

  return (
    <>
    <NavHeader setScreen={setScreen} />

    <main>
      {screen === "home" && <Home />}
      {screen === "login" && <Login />}
    </main>
    </>
  )
}

export default App
