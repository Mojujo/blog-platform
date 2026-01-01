import { useAuth } from "../../context/AuthContext";
import { useScreen } from "../../context/ScreenContext"
import styles from "./NavHeader.module.css"

export default function NavHeader() {
    const { setScreen } = useScreen();
    const { user, logout } = useAuth();

    return (
        <>
            <header className={styles.centerHeader}>
                <nav className={styles.navContainer}>
                    <div className={styles.homeContainer}>
                        <button
                            className={styles.navButton}
                            onClick={() => setScreen("home")}>
                            <img src="/assets/home.svg" alt="" className={styles.homeImage} />
                        </button>
                        <h2>Teknikbloggen</h2>
                    </div>
                    <div className={styles.accessButtons}>
                        {user && (
                            <button className={styles.navButton}
                                onClick={() => setScreen("profile")}>
                                <img src="/assets/profile.svg" alt="Profile" />
                            </button>
                        )}
                        {!user ? (
                            <>
                                <button className={styles.loginButton}
                                    onClick={() => setScreen("login")

                                    }>
                                    <img src="/assets/login.svg" alt="" className={styles.accessImage} />
                                    <p>Login</p>
                                </button>
                            </>
                        ) : (
                            <>
                                <button className={styles.logoutButton}
                                    onClick={async () => {
                                        logout();
                                        setScreen("home")
                                    }}>
                                    <img src="/assets/logout.svg" alt="" className={styles.accessImage} />
                                    <p>Logout</p>
                                </button>
                            </>
                        )}
                    </div>
                </nav>
            </header>
        </>
    )
}