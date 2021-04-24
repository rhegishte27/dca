import styled from 'styled-components';

export const Buttons = styled.div`
        display: flex;
        width: 100%;
        justify-content: flex-end;
        margin: auto;
        
        button {
            width: 15%;
        }
        
        button + button {
            margin-left: 7px;
        }
    `;