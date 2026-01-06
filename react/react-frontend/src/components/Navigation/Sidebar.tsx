import { useScreen } from "../../context/ScreenContext";
import styles from "./Sidebar.module.css"

export default function Sidebar() {

    const { screen, setScreen } = useScreen();


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
            </div>
        </>
    )
}