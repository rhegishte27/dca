import {X} from 'react-feather';
import styled from 'styled-components';

export const Nav = styled.nav`
    background-color: ${(props) => props.theme.colors.background.nav};
    color: ${(props) => props.theme.colors.text.primary};
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column;
    left: 0;
    height: calc(100% + 32px);
    width: 56px;
    z-index: 3;
`;

export const Panel = styled.div`
    background-color: ${(props) => props.theme.colors.background.panel};
    color: ${(props) => props.theme.colors.text.primary};
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column;
    overflow: hidden;
    overflow-y: auto;
    white-space: nowrap;
    height: 100%;
`;

export const Icon = styled.span`
    color: ${(props) => props.theme.colors.text.primary};
    min-width: 22px;

    > svg {
        height: 16px;
        width: 16px;
    }
`;

export const MainMenu = styled.ul`
    list-style: none;
    margin: 0;
    padding: 0;
    z-index: 1;

    & + ul {
        margin-top: auto;
    }

    hr {
        width: 80%;
    }
`;

export const MenuLink = styled.a`
    align-items: center;
    color: var(--c-black);
    cursor: pointer;
    display: flex;
    padding: 16px var(--s-double) 14px 19px;
    position: relative;
    text-decoration: none;

    & + ul > li > a {
        padding: var(--s-half) var(--s-double);

        &:hover {
            background: ${(props) => props.theme.colors.accent.main};
        }
    }
`;

export const MenuItem = styled.li`
    font-size: 14px;
    font-weight: var(--f-bold);
    letter-spacing: 1.02px;
    position: relative;

    &::before {
        background-color: ${(props) => props.theme.colors.accent.main};
        border-radius: 50%;
        content: '';
        display: block;
        height: 38px;
        left: 8px;
        position: absolute;
        top: 5px;
        transform-origin: 50% 50%;
        transition: all 0.25s;
        width: 38px;
        z-index: -1;
    }

    :not(.active) {
        &::before {
            transform: scale(0);
        }
    }

    &:hover {
        &::before {
            transform: scale(1);
        }
    }
`;

export const PanelContent = styled.div`
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
`;

export const PanelTitle = styled.div`
    font-size: 16px;
    line-height: 24px;
    padding: 7px 14px;
    font-weight: 600;
`;

export const PanelBreak = styled.hr`
    background-color: ${({ theme }) => theme.colors.borders.secondary};
    border: none;
    height: 2px;
    width: 95%;
    margin: 15px 0;
`;

export const CloseIcon = styled(X)`
    align-self: flex-end;
    margin: 5px;
    height: 16px;
    width: 16px;
    fill: red;
    color: red;
    cursor: pointer;
`;
