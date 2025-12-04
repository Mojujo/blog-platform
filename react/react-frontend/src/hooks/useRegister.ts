import apiClient from "../api/apiClient"

export function useRegiser() {
    const register = async (username: string, email: string, password: string) => {
        try {
            await apiClient.post("/user/register", {username, email, password});
            return true;
        } catch (err: any) {
            console.error("Registration Failed", err)
            return false
        }
    };

    return { register };
}