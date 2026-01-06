import { useAuth } from "../../context/AuthContext";
import { useScreen } from "../../context/ScreenContext";
import styles from "./Explore.module.css"

export default function Explore() {

    const { setScreen } = useScreen();
    const { user, logout } = useAuth();

    return (
        <>
            <div className={styles.exploreContainer}>
                {!user ? (
                    <>
                        <button className={styles.loginButton}
                            onClick={() => setScreen("login")

                            }>
                            <h3>Login</h3>
                            <img src="/assets/login.svg" alt="" className={styles.accessImage} />
                        </button>
                    </>
                ) : (
                    <>
                        <button className={styles.logoutButton}
                            onClick={async () => {
                                logout();
                                setScreen("home")
                            }}>
                            <h3>Logout</h3>
                            <img src="/assets/logout.svg" alt="" className={styles.accessImage} />
                        </button>
                    </>
                )}
            </div>
        </>
    )
}