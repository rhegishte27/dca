import React, {useState} from 'react';
import {Caret} from '../../general/caret/index';
import {useClickOutside} from '../../general/hooks/clickOutside';
import {Tag, TreeItem} from './styles/style';

interface CollapsibleProps {
    tag: string;
    title: string;
    children?: React.ReactElement;
}

const TreeItemContainer = ({tag, title, children}: CollapsibleProps) => {
    const [isOpen, setIsOpen] = useState(false);
    const [isSelected, setIsSelected] = useState(false);
    const [ref] = useClickOutside({ onClickOutside: () => setIsSelected(false) });

    const openFolder = (e: React.MouseEvent<HTMLSpanElement>): void => {
        e.stopPropagation();
        setIsOpen(!isOpen);
        setIsSelected(true);
    };

    const contextMenu = (e: any) => {
        e.preventDefault();
        // TODO: Open contextual menu on right click to display folder options
    };

    return (
        <>
            <TreeItem role="treeitem" isClicked={!isSelected}>
                <div ref={ref} onClick={openFolder} onContextMenu={contextMenu}>
                    <Caret isExpand={isOpen} />
                    <Tag>{tag}</Tag>
                    {title}
                </div>
                {isOpen && <>{children}</>}
            </TreeItem>
        </>
    );
};

export default TreeItemContainer
