import { TreeFile, Tag, Path } from './styles/style';
import React, { useState } from 'react';
import { useClickOutside } from '../../general/hooks/clickOutside';

interface CollapsibleProps {
    tag?: string;
    title: string;
    path?: string;
    children?: React.ReactElement;
}

const TreeFileContainer = ({ tag, title, path }: CollapsibleProps) => {
    const [isSelected, setIsSelected] = useState(false);
    const [ref] = useClickOutside({ onClickOutside: () => setIsSelected(false) });

    const onDoubleClick = () => {
        // open the file
    };

    const handleIsSelected = (e: React.MouseEvent<HTMLAnchorElement>): void => {
        e.stopPropagation();
        setIsSelected(true);
    };

    const contextMenu = (e: any) => {
        e.preventDefault();
        // TODO: Open contextual menu on right click to display file options
    };

    return (
        <li role="none">
            <TreeFile
                role="treeitem"
                isClicked={!isSelected}
                ref={ref}
                onClick={handleIsSelected}
                onDoubleClick={onDoubleClick}
                onContextMenu={contextMenu}
            >
                <Tag>{tag}</Tag>
                {title}
                <Path>{path}</Path>
            </TreeFile>
        </li>
    );
};

export default TreeFileContainer
