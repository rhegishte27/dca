import React from 'react';
import { Icon, MenuItem, MenuLink } from './style';

interface MenuHeaderProps {
    icon: React.ReactElement;
    title: string;
    href?: any;
    className?: string;
    children?: React.ReactElement;
    active?: boolean;
    shortcut?: string[];

    onClick?(): void;
}

export const NavBarItem = ({
                               icon,
                               title,
                               children,
                               href,
                               onClick = () => {
                               },
                               active,
                           }: MenuHeaderProps) => {

    return (
        <MenuItem className={active ? 'active' : ''}>
            <MenuLink
                onClick={onClick}
                {...(href ? {} : {role: 'button'})}
                href={href}
                title={title}
            >
                <Icon>{icon}</Icon>
                {children}
            </MenuLink>
        </MenuItem>
    );
};

export default NavBarItem;
