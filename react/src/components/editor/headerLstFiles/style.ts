import styled from 'styled-components';

export const HeaderContainer = styled.header`
    background-color: ${(props) => props.theme.colors.background.nav};
    color: ${(props) => props.theme.colors.text.primary};
    display: flex;
    justify-content: start;
    grid-area: header;
    border: 1px solid;
    z-index: 4;
`;

export const HeaderElements = styled.ul`
    list-style: none;
    position: relative;
    display: flex;
    margin: 0;
    padding: 0;

    & > li {
        padding: 0 8px;
        display: inline-flex;
        justify-content: center;
        align-items: center;
        border: 1px solid;
    }
`;

export const HeaderElement = styled.button`
    padding: 0px;
`;

export const MenusContainer = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-grow: 1;
    padding-right: 16px;
`;
