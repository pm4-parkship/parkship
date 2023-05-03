import { useEffect } from "react";
import { useRouter } from "next/router";

export function useAuthRedirect(pageProps: any, userData: any) {
    const router = useRouter();
    const { isInitialized, isSignedIn } = userData;

    useEffect(() => {
        if (!router.isReady || !isInitialized) return;

        if (!isSignedIn && !router.asPath.includes("login")) {
            router.replace(`/login`, undefined, { shallow: true });
        }
    }, [pageProps, router.isReady, isInitialized, isSignedIn]);
}
