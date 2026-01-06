import { createContext, useState, ReactNode, useContext, useEffect } from "react";
import { getXsrfToken } from "../util/csrfUtil";
import apiClient from "../api/apiClient";

type User = {
    userId: string;
    userProfilePictureUrl: string;
    username: string;
    roles: string[];
};

type AuthContextType = {
    user: User | null;
    isLoggedIn: boolean;
    login: (username: string, password: string) => Promise<boolean>;
    logout: () => void;
    authLoaded: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {

    const [user, setUser] = useState<User | null>(null);
    const [authLoaded, setAuthLoaded] = useState(false);

    // Axios interceptor to automatically attach CSRF tokens
    useEffect(() => {
        const interceptor = apiClient.interceptors.request.use(async config => {

            if (!config.headers) {
                config.headers = {} as any;
            }

            if (config.url !== "/user/csrf") {
                try {
                    // Fetch CSRF token if not present
                    if (!getXsrfToken()) {
                        await apiClient.get("/user/csrf");
                    }
                } catch (err: any) {
                    console.error("Failed to fetch CSRF token", err)
                }
            }

            const token = getXsrfToken();
            if (token) {
                (config.headers as any)["X-XSRF-TOKEN"] = token;
            }

            config.withCredentials = true; // always send cookies
            return config;
        });

        return () => {
            apiClient.interceptors.request.eject(interceptor);
        };
    }, []);

    const refreshUser = async () => {

        if (localStorage.getItem("loggedOut") === "true") {
            setUser(null);
            setAuthLoaded(true);
            return;
        }

        try {
            const response = await apiClient.get("/auth/me");
            setUser(prev => {
                if (prev) return prev;

                return {
                    userId: response.data.id,
                    userProfilePictureUrl: response.data.profileImageUrl,
                    username: response.data.username,
                    roles: response.data.roles
                };
            });

        } catch (err: any) {
            if (err.response?.status === 401) {
                setUser(null);
            } else {
                console.error("Failed to refresh user", err)
            }
        } finally {
            setAuthLoaded(true);
        }
    };

    useEffect(() => {
        refreshUser();
    }, []);

    const login = async (username: string, password: string): Promise<boolean> => {
        try {
            const response = await apiClient.post("/auth/login", { username, password });

            setUser({
                userId: response.data.id,
                userProfilePictureUrl: response.data.profileImageUrl,
                username: response.data.username,
                roles: response.data.roles
            });

            localStorage.removeItem("loggedOut");

            return true;

        } catch (err: any) {
            setUser(null);
            throw err;
        }
    };

    const logout = async (): Promise<void> => {

        try {
            await apiClient.post("auth/logout");

        } catch (err: any) {
            console.error("Logout Failed", err)
        } finally {
            setUser(null);
            localStorage.setItem("loggedOut", "true");
            localStorage.removeItem("screen");
        }


    };

    return (
        <AuthContext.Provider value={{
            user,
            isLoggedIn: user !== null,
            login,
            logout,
            authLoaded
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