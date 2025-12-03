import { useAuth } from "../context/AuthContext";
import { Screen } from "../App";
import styles from "./NavHeader.module.css"

export default function NavHeader({ setScreen }: { setScreen: (s: Screen) => void }) {
    const { user, logout } = useAuth();

    return (
        <>
            <header className={styles.centerHeader}>
                <nav className={styles.navContainer}>
                    <button
                        className={styles.homeButton}
                        onClick={() => setScreen("home")}>
                        <img src="./assets/home.svg" alt="" className={styles.homeImage} />
                    </button>

                    <h2>
                        Min fina blog-platform
                    </h2>

                    <button
                        onClick={() => {
                            if (!user) {
                                setScreen("login");
                            } else {
                                logout();
                                setScreen("home");
                            }
                        }}
                    >
                        {user ? "Logout" : "Login"}
                    </button>

                </nav>
            </header>
        </>
    )
}