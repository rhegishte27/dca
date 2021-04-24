import styled from 'styled-components';

export const FooterStatusContainer = styled.footer`
    text-align: center;
    background-color: ${({ theme }) => theme.colors.background.nav};
    color: ${({ theme }) => theme.colors.text.primary};
    font-size: 15px;
    grid-area: footer;
    border: 1px solid;
    z-index: 2;
`;

export const FooterStatusElement = styled.text`
    color: red;
`;

