import { useEffect, useRef, DependencyList } from 'react';

interface ClickOutsideProps {
    onClickOutside(): void;
}

export const useClickOutside = ({ onClickOutside }: ClickOutsideProps, dep: DependencyList = []) => {
    const node = useRef<any>(null);

    useEffect(() => {
        document.addEventListener('mousedown', handleClick);

        return () => {
            document.removeEventListener('mousedown', handleClick);
        };
    }, dep);

    const handleClick = (e: any) => {
        if (node.current && !node.current.contains(e.target)) {
            onClickOutside();
        }
    };

    return [node];
};
