import { useEffect, useRef } from "react";

export const useAutoResizeTextarea = (value: string) => {
    const ref = useRef<HTMLTextAreaElement>(null);

    const resize = () => {
        if (!ref.current) return;
        ref.current.style.height = "auto";
        ref.current.style.height = ref.current.scrollHeight + "px";
    };

    useEffect(() => {
        resize();
    }, [value]);

    return { ref, resize };
};