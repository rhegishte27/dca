import styled from 'styled-components';

export const Collapse = styled.div``;

export const CollapseHeader = styled.header<{ level: number }>`
    background-color: ${({ theme, level }) =>
        level === 0 ? theme.colors.background.panelHeader : theme.colors.background.subPanelHeader};
    color: ${(props) => props.theme.colors.text.primary};
    border-bottom: 1px solid ${(props) => props.theme.colors.borders.primary};
    cursor: pointer;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    font-size: 14px;
    font-weight: 600;
    height: 30px;
    padding: 4px;
    white-space: nowrap;
    width: 100%;
`;

export const HeaderActions = styled.div`
    align-items: center;
    display: flex;
    margin-left: auto;

    button {
        padding-bottom: 0;
        padding-top: 0;
        height: 21px;
    }
`;

export const CollapseContent = styled.div`
    width: 100%;
`;
