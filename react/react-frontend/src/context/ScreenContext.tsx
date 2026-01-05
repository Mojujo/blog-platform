import { createContext, ReactNode, useContext, useState } from "react";

export type Screen = "home" | "login" | "register" | "profile" | "createPost" | "bookmarks";

type ScreenContextType = {
    screen: Screen;
    setScreen: (s: Screen) => void;
};

const ScreenContext = createContext<ScreenContextType | undefined>(undefined);

export const ScreenProvider = ({ children }: {children: ReactNode}) => {
    const [screen, setScreenState] = useState<Screen>(
        () => (localStorage.getItem("screen") as Screen) || "home"
    );

    const setScreen = (s: Screen) => {
        localStorage.setItem("screen", s);
        setScreenState(s);
    }

    return (
        <ScreenContext.Provider value={{screen, setScreen}}>
            {children}
        </ScreenContext.Provider>
    );
};

export const useScreen = () => {
    const ctx = useContext(ScreenContext);
    if (!ctx) throw new Error("Must be used withing ScreenProvider");
    return ctx;
};