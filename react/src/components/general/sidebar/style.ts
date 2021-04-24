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

