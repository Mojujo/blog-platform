import { useState } from 'react';
import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import NavHeader from './components/NavHeader';
import Profile from './pages/Profile';
import CreatePost from './pages/CreatePost';

export type Screen = "home" | "login" | "register" | "profile" | "createPost";

function App() {
  const [screen, setScreen] = useState<Screen>("home");

  return (
    <>
      <NavHeader setScreen={setScreen} />

      <main>
        {screen === "home" && <Home />}
        {screen === "login" && <Login setScreen={setScreen} />}
        {screen === "register" && <Register setScreen={setScreen} />}
        {screen === "profile" && <Profile setScreen={setScreen} />}
        {screen === "createPost" && <CreatePost setScreen={setScreen} />}
      </main>
    </>
  )
}

export default App
