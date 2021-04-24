import React, { useState, ReactElement } from 'react';
import { Collapse, CollapseHeader, HeaderActions, CollapseContent } from './styles/collapse';
import { Caret } from '../../general/caret/index';

interface CollapsibleProps {
    title: string;
    children: ReactElement;
    defaultOpened?: boolean;
    actions?: ReactElement | null;
    level?: number;
    header?(props: CollapseHeaderProps): ReactElement;
}

export interface CollapseHeaderProps {
    title: string;
    children?: React.ReactElement;
    isOpen: boolean;
    level?: number;

    onClick?(): void;
}

const CollapseHeaderContainer = ({ title, children, onClick, isOpen, level = 0 }: CollapseHeaderProps) => {
    return (
        <CollapseHeader onClick={onClick} level={level}>
            <Caret isExpand={isOpen} />
            {title}
            <HeaderActions>{children}</HeaderActions>
        </CollapseHeader>
    );
};

const CollapseContainer = ({
    title,
    children,
    defaultOpened,
    actions,
    level = 0,
    header: Header = CollapseHeaderContainer,
}: CollapsibleProps) => {
    const [isOpen, setIsOpen] = useState(defaultOpened || false);

    const toggleCollapse = () => {
        setIsOpen(!isOpen);
    };

    return (
        <Collapse>
            <Header level={level} title={title} onClick={toggleCollapse} isOpen={isOpen}>
                {actions || <></>}
            </Header>
            {isOpen && <CollapseContent>{children}</CollapseContent>}
        </Collapse>
    );
};

export default CollapseContainer;
