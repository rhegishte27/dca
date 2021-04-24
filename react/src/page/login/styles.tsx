import styled from 'styled-components';

export const Wrapper = styled.div`
    width: 100vw;
    height: 100vh;

    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;

    & > * {
        width: 100%;
    }
`;

export const Header = styled.div`
    display: flex;
    flex: 1 1 15vh;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    margin-top: 10vh;

    & > h1 {
        color: grey;
        font-size: 24px;
    }

    & > img {
        width: 25rem;
    }
`;

export const ErrorMessage = styled.p`
    color: red;
`;

export const LoginForm = styled.form`
    display: flex;
    flex: 2 1 40vh;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    label {
        display: flex;
        flex-direction: column;
        margin: auto;
    }

    input,
    select {
        width: 300px;
        height: 30px;
        line-height: 27px;
        border-radius: 5px;
        border: 1px solid #504f4f;
        padding: 0px 7px;
        font-size: 14px;
        margin-bottom: 20px;
        position: relative;
        background-color: transparent;
    }

    input + button {
        position: absolute;
        width: 28px;
        height: 28px;
        border: 0;
        margin: 0 -30px;
        padding: 2px;
        background-color: transparent;
        cursor: pointer;
    }
`;

export const SignInButton = styled.button`
    width: 300px;
    height: 50px;
    line-height: 45px;
    vertical-align: middle;
    text-align: center;
    padding: 0px;
    border-radius: 25px;
    border: 0;
    background-color: #ef483e;
    color: white;
    font-size: 20px;
    font-weight: 550;
    margin: auto;
    cursor: pointer;

    &:disabled {
        cursor: not-allowed;
        background-color: white;
        border: 2px solid #ef483e;
        color: #ef483e;
    }
`;

export const Footer = styled.div`
    display: flex;
    flex: 1 1 15vh;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 10vh;
`;

export const Build = styled.div`
    font-weight: 550;
`;

export const Legal = styled.div`
    margin-top:20px;
    text-align: center;
    color: grey;
`;
