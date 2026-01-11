import { useAuth } from "../../context/AuthContext";
import { useScreen } from "../../context/ScreenContext";
import { useProfile } from "../../hooks/useProfile";
import styles from "./Sidebar.module.css"

export default function Sidebar() {

    const { screen, setScreen } = useScreen();
    const { user, logout } = useAuth();
    const { profile } = useProfile();

    return (
        <>
            <div className={styles.sidebarContainer}>
                <button className={`${styles.navButton} ${screen === "home" ? styles.active : ""}`}
                    onClick={() => setScreen("home")}>
                    <img src="/assets/home.svg" alt="" className={styles.homeImage} />
                    <h3>Home</h3>
                </button>
                <button className={`${styles.navButton} ${screen === "profile" ? styles.active : ""}`}
                    onClick={() => setScreen("profile")}>
                    <img src="/assets/profile.svg" alt="" className={styles.homeImage} />
                    <h3>Profile</h3>
                </button>
                <button className={`${styles.navButton} ${screen === "bookmarks" ? styles.active : ""}`}
                    onClick={() => setScreen("bookmarks")}>
                    <img src="/assets/bookmark.svg" alt="" className={styles.homeImage} />
                    <h3>Bookmarks</h3>
                </button>

                {user && (
                    <>
                        <button className={styles.sidebarProfile}
                            onClick={() => setScreen("profile")}>
                            <img
                                src={profile?.profileImageUrl}
                                alt="Profile"
                            />
                        </button>
                    </>
                )}

                {!user ? (
                    <>
                        <button className={styles.sidebarLogin}
                            onClick={() => setScreen("login")

                            }>
                            <img src="/assets/login.svg" alt="" className={styles.accessImage} />
                            <h4>Login</h4>
                        </button>
                    </>
                ) : (
                    <>
                        <button className={styles.sidebarLogout}
                            onClick={async () => {
                                logout();
                                setScreen("home")
                            }}>
                            <img src="/assets/logout.svg" alt="" className={styles.accessImage} />
                            <h4>Logout</h4>
                        </button>
                    </>
                )}
            </div>
        </>
    )
}