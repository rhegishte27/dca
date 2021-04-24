import styled from 'styled-components';

export const FooterButtonsContainer = styled.div`
    display: flex;
    background-color: ${({ theme }) => theme.colors.background.nav};
    color: ${({ theme }) => theme.colors.text.primary};
    font-size: 12px;
    border: 1px solid;
`;

export const FooterButtonElement = styled.button`
    padding: 0px;
`;

export const FooterButtonsElementRight = styled.div`
    justify-content: space-between;
`;

export const FooterButtonsElementLeft = styled.div`
    justify-content: space-between;
`;
