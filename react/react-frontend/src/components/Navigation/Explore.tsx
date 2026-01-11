import { useAuth } from "../../context/AuthContext";
import { useScreen } from "../../context/ScreenContext";
import { useProfile } from "../../hooks/useProfile";
import styles from "./Explore.module.css"

export default function Explore() {

    const { setScreen } = useScreen();
    const { user, logout } = useAuth();
    const { profile } = useProfile();


    return (
        <>
            <div className={styles.exploreContainer}>
                {user && profile && (
                    <>
                        <button className={styles.profileShortcut}
                            onClick={() => setScreen("profile")}>
                            <img
                                src={`${profile.profileImageUrl}?t=${Date.now()}`}
                                alt="Profile"
                            />
                        </button>
                    </>
                )}

                {!user ? (
                    <>
                        <button className={styles.loginButton}
                            onClick={() => setScreen("login")

                            }>
                            <img src="/assets/login.svg" alt="" className={styles.accessImage} />
                            <h3>Login</h3>
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
                            <h3>Logout</h3>
                        </button>
                    </>
                )}
            </div>
        </>
    )
}