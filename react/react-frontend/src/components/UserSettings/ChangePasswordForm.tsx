import { useUserEdit } from "../../hooks/useUserEdit.ts";
import { useState } from "react";

export const ChangePasswordForm = () => {
    const { changePassword, status, error } = useUserEdit();
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [success, setSuccess] = useState(false);

    const submit = async () => {
        const ok = await changePassword(oldPassword, newPassword);
        setSuccess(ok);

        if (ok) {
            setOldPassword("");
            setNewPassword("");
        }
    }

    return (
        <>
            <div>
                <h3>Change Password</h3>
                <input
                    value={oldPassword}
                    name="password-old-edit"
                    onChange={(e) => setOldPassword(e.target.value)}
                    placeholder="Current Password"
                    type="password"
                />
                <input
                    value={newPassword}
                    name="password-new-edit"
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="New Password"
                    type="password"
                />
                <button onClick={submit} disabled={status === "loading" || !oldPassword || !newPassword}>Update Password
                </button>
                {success && <p>Password updated. You are being logged out.</p>}
                {error && <p>{error}</p>} </div>
        </>
    )
}