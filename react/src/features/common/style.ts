import styled from 'styled-components';

export const SmallForm = styled.form`
    width: 40%;
    padding: 0 14px;
`;

export const MediumForm = styled.form`
    width: 60%;
    padding: 0 14px;
`;

export const LargeForm = styled.form`
    width: 100%;
    padding: 0 14px;
`;

export const FormTitle = styled.div`
    font-size: 24px;
    line-height: 24px;
    margin-top: 32px;
    padding-top: 7px;
    padding-bottom: 7px;
    font-weight: 600;
`;

export const PanelBreak = styled.hr`
    border: none;
    height: 2px;
    width: 95%;
    margin: 15px 0;
`;

export const DataObjectTableResult = styled.table`
    table-layout: fixed;
    word-wrap:break-word;
    width: 100%;
    border-collapse: collapse;
    text-align: left;
    padding-left: 0;
    margin-top: 7px;

    thead {
        th {
            font-weight: 600;
        }
        th:first-child {
            width: 25%;
        }
        th:nth-child(2) {
            width: 20%;
        }
    }
    
    tbody {
        tr:hover:nth-child(n) {
            background-color: #F5F5F5;
        }
        tr>td {
            padding-bottom: 1em;
        }
    }
    
    tbody:before {
        content:"@";
        display:block;
        line-height:10px;
        text-indent:-99999px;
    }
`;

export const DataObjectTable = styled.table`
    table-layout: fixed;
    word-wrap:break-word;
    width: 100%;
    border-collapse: collapse;
    text-align: left;
    padding-left: 0;
    margin-top: 7px;

    thead {
        th {
            font-weight: 600;
        }
        th:first-child {
            width: 20%;
        }
        th:nth-child(2) {
            width: 20%;
        }
        th:nth-child(3) {
            width: 13%;
        }
    }
    
    tbody {
        tr:hover:nth-child(n) {
            background-color: #F5F5F5;
        }
        tr>td {
            padding-bottom: 1em;
        }
    }
    
    tbody:before {
        content:"@";
        display:block;
        line-height:10px;
        text-indent:-99999px;
    }
`;

export const TableAndActions = styled.table`
    table-layout: fixed;
    word-wrap:break-word;
    width: 100%;
    border-collapse: collapse;
    text-align: left;
    padding-left: 0;
    margin-top: 7px;

    thead {
        th {
            font-weight: 600;
        }
        th:last-child {
            width: 22%;
        }
        th:first-child {
            width: 4%;
        }
    }
    
    tbody {
        tr:hover:nth-child(n) {
            background-color: #F5F5F5;
        }
    }
    
    tbody:before {
        content:"@";
        display:block;
        line-height:10px;
        text-indent:-99999px;
    }
`;

export const Actions = styled.div`
    button + button {
        margin-left: 7px;
    }
`;

export const OptionCheckboxWrapper = styled.div`
    display: flex;
    padding-right: 30px;
    align-items: center;

    & > input {
        margin-left: 0px;
    }

    & > label {
        font-size: 14px;
        letter-spacing: 0.4px;
    }
`;

export const ErrorMessage = styled.span`
    color: red;
    font-size: small;
`;

export const OptionSelectionWrapper = styled.div`
    margin: 8px 0px;
`;

export const OptionWrapper = styled.div`
    font-size: 12px;
    line-height: 20px;
    letter-spacing: 0.2px;
`;

export const Table = styled.table`
    table-layout: fixed;
    width: 100%;
    td {
        text-align: left;
    }
`;
