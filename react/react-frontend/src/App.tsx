import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Profile from './pages/Profile';
import CreatePost from './pages/CreatePost';
import { useScreen } from './context/ScreenContext';
import Sidebar from './components/Navigation/Sidebar';
import Explore from './components/Navigation/Explore';

function App() {
  const { screen } = useScreen();

  return (
    <>
      <div className="contentWrapper">
        <Sidebar />
        {screen === "home" && <Home />}
        {screen === "login" && <Login />}
        {screen === "register" && <Register />}
        {screen === "profile" && <Profile />}
        {screen === "createPost" && <CreatePost />}
        <Explore />
      </div>
    </>
  )
}

export default App
