import { createContext, useState, ReactNode, useContext } from "react";
import { getXsrfToken } from "../util/csrfUtil";
import apiClient from "../api/apiClient";

type User = {
    username: string;
    roles: string[];
};

type AuthContextType = {
    user: User | null;
    isLoggedIn: boolean;
    login: (username: string, password: string) => Promise<boolean>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {

    const [user, setUser] = useState<User | null>(null);

    const login = async (username: string, password: string): Promise<boolean> => {

        const xsrfToken = getXsrfToken();

        try {
            const response = await apiClient.post("/auth/login",
                { username, password },
                {
                    headers: { "X-XSRF-TOKEN": xsrfToken || "" },
                    withCredentials: true
                }
            );

            setUser({
                username: response.data.username,
                roles: response.data.roles
            });

            return true;

        } catch (err: any) {
            setUser(null);
            return false;
        }
    };

    const logout = async (): Promise<void> => {

        const xsrfToken = getXsrfToken();

        try {
            await apiClient.post("auth/logout",
                {},
                {
                    headers: { "X-XSRF-TOKEN": xsrfToken || "" },
                    withCredentials: true
                }
            );

        } catch (err: any) {
            console.error("Logout Failed", err)
        }

        setUser(null);
    };

    return (
        <AuthContext.Provider value={{
            user,
            isLoggedIn: user !== null,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("Must be used within AuthProvider");
    return ctx;
};