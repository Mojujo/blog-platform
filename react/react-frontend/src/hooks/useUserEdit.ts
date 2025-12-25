import { useState } from "react"
import apiClient from "../api/apiClient";

export const useUserEdit = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const patch = async (url: string, body: object) => {
        setLoading(true);
        setError(null);

        try {
            await apiClient.patch(url, body);
            return true;
        } catch (err: any) {
            const msg =
                err?.response?.data?.message ??
                err?.response?.data ?? "Request failed";
            setError(msg);
            return false;
        } finally {
            setLoading(false);
        }
    };

    return {
        loading,
        error,
        changeUsername: (newUsername: string) => 
            patch("/user/username", {newUsername}),
        changeEmail: (newEmail: string) =>
            patch("/user/email", {newEmail}),
        changePassword: (oldPassword: string, newPassword: string) =>
            patch("/user/password", {oldPassword, newPassword})
    }
}