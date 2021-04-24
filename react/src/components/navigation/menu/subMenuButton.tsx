import React, { MouseEvent } from 'react';
import { Icon, RIcon, Shortcut, Title } from './style';

interface ButtonProps {
    icon?: React.ReactElement;
    rIcon?: React.ReactElement;
    title: React.ReactNode;
    shortcut?: string;
    disabled?: boolean;
    href?: string;
    target?: string;

    onClick?(e: MouseEvent<HTMLAnchorElement>): void;
}

const SubMenuButton = ({ icon, rIcon, title, shortcut, disabled, onClick, ...props }: ButtonProps) => {
    const handleClick = (e: MouseEvent<HTMLAnchorElement>) => {
        onClick && onClick(e);
    };
    const aStyle = disabled ? { cursor: 'default' } : {};
    const titleStyle = disabled ? {opacity: '.5'} : {};
    const onClickHandler = disabled ? () => {} : handleClick;

    return (
        <a role="menuitem" style={aStyle} onClick={onClickHandler} {...props}>
            <Title style={titleStyle}>
                <Icon>{icon}</Icon>
                {title}
                <RIcon>{rIcon}</RIcon>
            </Title>
            <Shortcut>{shortcut}</Shortcut>
        </a>
    );
};

export default SubMenuButton;
