import { File, Folder } from 'react-feather';
import styled, { css } from 'styled-components';

export const TreeContainer = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-top: 1px solid ${({theme}) => theme.colors.borders.secondary};
    border-bottom: 1px solid ${({theme}) => theme.colors.borders.secondary};

    & > div {
        width: 100%;
        height: 100%;
    }
`;

const TreeIconStyle = css`
    width: 20px;
    height: 20px;
    margin-right: 4px;
`;

export const TreeFolderIcon = styled(Folder)`
    ${TreeIconStyle}
`;

export const TreeFileIcon = styled(File)`
    ${TreeIconStyle}
`;