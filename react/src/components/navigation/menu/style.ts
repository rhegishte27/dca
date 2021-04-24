import styled from 'styled-components';

/* stylelint-disable no-descending-specificity*/
export const Nav = styled.nav`
    padding-left: 15px;
    ul {
        display: flex;
        list-style: none;
        margin: 0;
        padding: 0;
    }
`;

export const NavBarMenuItem = styled.li`
    display: block;
    position: relative;

    &:hover {
        background-color: var(--c-editor-light-2);

        button,
        a {
            color: var(--c-editor-dark-1);
        }

        & > ul {
            display: inline;

            & > ul {
                display: none;
            }
        }
    }

    button,
    a {
        align-items: center;
        background: transparent;
        border: none;
        color: var(--c-editor-light-3);
        cursor: pointer;
        font-size: 12px;
        font-weight: 600;
        display: flex;
        justify-content: space-between;
        line-height: 32px;
        padding: 0 var(--s-double);
        width: 100%;

        &:hover + ul {
            /*display: block;*/
        }
    }

    ul {
        background-color: var(--c-editor-light-3);
        display: none;
        flex-direction: column;
        position: absolute;
        white-space: nowrap;
        z-index: 9999;

        li {
            button,
            a {
                color: var(--c-editor-dark-1);
                display: inline-flex;

                &:hover {
                    background-color: var(--c-editor-light-2);
                }

                &:disabled {
                    opacity: 0.5;

                    &:hover {
                        background-color: transparent;
                        cursor: default;
                    }
                }
            }
        }

        hr {
            background-color: var(--c-editor-light-2);
            border: none;
            height: 2px;
            margin: 0;
        }
    }
`;

export const Title = styled.span`
    align-items: center;
    display: flex;
`;

export const Icon = styled.span`
    display: inline-block;
    font-size: 16px;
    font-weight: var(--f-bold);
    margin-right: var(--s-base);
    width: var(--s-double);

    &:empty {
        margin: 0;
        width: 0;
    }

    > svg {
        height: 14px;
        width: 14px;
    }
`;

export const RIcon = styled.span`
    display: inline-block;
    font-size: 16px;
    font-weight: var(--f-bold);
    margin-left: var(--s-base);
    width: var(--s-double);

    &:empty {
        margin: 0;
        width: 0;
    }

    > svg {
        height: 14px;
        width: 14px;
    }
`;

export const Shortcut = styled.span`
    color: var(--c-editor-light-1);
    font-size: 10px;
    font-weight: var(--f-normal);
    margin-left: var(--s-base);
    text-transform: uppercase;
`;

interface MenuDropDownProps {
    show: boolean;
    align?: string;
}

export const MenuDropDown = styled.div<MenuDropDownProps>`
    display: ${({ show }) => (show ? 'block' : 'none')};
    background-color: ${({ theme }) => theme.colors.background.panel};
    color: ${({ theme }) => theme.colors.text.primary};
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.5);
    position: absolute;
    top: 100%;
    right: ${({ align }) => (align === 'right' ? 'auto' : '0')};
    z-index: 99;
    width: min-content;
    border: 1px solid ${({ theme }) => theme.colors.borders.primary};
    border-radius: 2%;

    ul {
        list-style: none;
        padding: 5px;
        margin: 0;
        width: min-content;

        li + li {
            border-top: 1px dotted ${({ theme }) => theme.colors.borders.primary};
        }

        li {
            cursor: pointer;
            width: 100%;
            padding: 5px 0;
        }
        li:hover {
            background-color: ${({ theme }) => theme.colors.accent.main};
            color: white;
        }
    }
`;

export const MenuToggle = styled.div`
    display: inline-flex;
    align-items: center;
    text-align: center;
    user-select: none;
    white-space: nowrap;
    position: relative;

    span.caret {
        width: 22px;
        height: 22px;
    }

    &::before {
        background-color: ${(props) => props.theme.colors.accent.main};
        border-radius: 10%;
        content: '';
        display: block;
        top: -3px;
        left: -3px;
        height: calc(100% + 6px);
        position: absolute;
        transform-origin: 50% 50%;
        transition: all 0.25s;
        width: calc(100% + 6px);
        z-index: -1;
    }

    :not(.active) {
        &::before {
            transform: scale(0);
        }
    }

    &:hover {
        cursor: pointer;
        &::before {
            transform: scale(1);
        }
    }
`;
/* stylelint-enable */
