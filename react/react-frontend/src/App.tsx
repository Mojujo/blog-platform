import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Profile from './pages/Profile';
import CreatePost from './pages/CreatePost';
import { useScreen } from './context/ScreenContext';
import Sidebar from './components/Navigation/Sidebar';
import Explore from './components/Navigation/Explore';
import EditUser from './pages/EditUser';
import Bookmarks from './pages/Bookmarks';

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
        {screen === "editUser" && <EditUser />}
        {screen === "bookmarks" && <Bookmarks />}
        <Explore />
      </div>
    </>
  )
}

export default App
