import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import NavHeader from './components/NavHeader';
import Profile from './pages/Profile';
import CreatePost from './pages/CreatePost';
import { useScreen } from './context/ScreenContext';

function App() {
  const { screen } = useScreen();

  return (
    <>
      <NavHeader />

      <main>
        {screen === "home" && <Home />}
        {screen === "login" && <Login />}
        {screen === "register" && <Register />}
        {screen === "profile" && <Profile />}
        {screen === "createPost" && <CreatePost />}
      </main>
    </>
  )
}

export default App
