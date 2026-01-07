import { ChangeEmailForm } from "../components/UserSettings/ChangeEmailForm"
import { ChangePasswordForm } from "../components/UserSettings/ChangePasswordForm"
import { ChangeProfilePictureForm } from "../components/UserSettings/ChangeProfilePictureForm"
import { ChangeUsernameForm } from "../components/UserSettings/ChangeUsernameForm"
import styles from "./EditUser.module.css"

export default function EditUser() {
    return (
        <>
            <div className={styles.editContainer}>
                <ChangeUsernameForm />
                <ChangeEmailForm />
                <ChangePasswordForm />
                <ChangeProfilePictureForm />
            </div>
        </>
    )
}