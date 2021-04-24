import React from 'react';

const useBeforeUnload = (handleUnload: (event: BeforeUnloadEvent) => void) => {
    React.useEffect(() => {

        window.onbeforeunload = handleUnload;
        return () => {
            window.onbeforeunload = null;
        };
    }, []);
};

export default useBeforeUnload;