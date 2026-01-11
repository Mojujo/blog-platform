import { useState } from "react"
import apiClient from "../api/apiClient";
import { useAuth } from "../context/AuthContext";

type Status = "idle" | "loading" | "success" | "error";

export const useUserEdit = () => {
    const [status, setStatus] = useState<Status>("idle");
    const [error, setError] = useState<string | null>(null);
    const { logout } = useAuth();

    const patch = async (url: string, body: object) => {
        
        setStatus("loading")
        setError(null);

        try {

            await apiClient.patch(url, body);
            setStatus("success");
            logout();
            return true;

        } catch (err: any) {

            setStatus("error");
            setError(err?.response?.data?.detail ?? "Error updating profile");
            return false;

        }
    };

    const postFile = async (url: string, file: File) => {
        setStatus("loading");
        setError(null);

        try {

            const formData = new FormData();
            formData.append("file", file);

            await apiClient.post(url, formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });

            setStatus("success");
            return true;
        } catch (err: any) {
            setStatus("error");
            setError(err?.response?.data?.detail ?? "Error uploading file");
            return false;
        }
    };

    return {
        status,
        error,
        changeUsername: (newUsername: string) =>
            patch("/user/username", { newUsername }),
        changeEmail: (newEmail: string) =>
            patch("/user/email", { newEmail }),
        changePassword: (oldPassword: string, newPassword: string) =>
            patch("/user/password", { oldPassword, newPassword }),
        changeProfilePicture: (file: File) => 
            postFile("/user/profile-picture", file)
    };
}