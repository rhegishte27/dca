import React, { ChangeEvent, FormEvent, useContext, useEffect, useRef, useState } from 'react';
import { Eye, EyeOff } from 'react-feather/dist';
import { AuthContext } from '../authContext';
import { Build, ErrorMessage, Footer, Header, Legal, LoginForm, SignInButton, Wrapper } from './styles';

const Login = () => {
    const identifierInput = useRef<HTMLInputElement>(null);
    const [identifier, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [showPass, setShowPass] = useState(false);
    const [loading, setLoading] = useState<boolean>(false);

    const [errorMsg, setErrorMsg] = useState('');
    const {login} = useContext(AuthContext);

    const validForm = identifier && password;

    const submitFormLogin = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);

        if (validForm) {
            try {
                await login(identifier, password);
            } catch (error) {
                const err = error[0];
                setErrorMsg(err.message);
                setUserName('');
                setPassword('');
                setLoading(false);
            }
        } else {
            setErrorMsg('Please fill-in all the fields');
        }
    };

    const handleIdentifierChange = (e: ChangeEvent<HTMLInputElement>) => {
        const name = e.target.value;
        setUserName(name);
    };

    useEffect(() => {
        identifierInput.current?.focus();
    }, []);

    return (
        <Wrapper>
            <Header>
                <img src="./dca-logo.jpg" alt="Equisoft/dca"/>
                <h1>Sign in</h1>
            </Header>
            <LoginForm onSubmit={submitFormLogin}>
                {errorMsg && <ErrorMessage>{errorMsg}</ErrorMessage>}
                <label>
                    Identifier
                    <input
                        ref={identifierInput}
                        name="identifier"
                        value={identifier}
                        onChange={handleIdentifierChange}
                    />
                </label>
                <label>
                    Password
                    <div>
                        <input
                            type={showPass ? 'text' : 'password'}
                            name="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <button type="button" onClick={() => setShowPass(!showPass)} tabIndex={-1}>
                            {showPass ? <EyeOff /> : <Eye />}
                        </button>
                    </div>
                </label>

                <SignInButton type="submit">
                    {loading ? 'Signing in ...' : 'Sign in'}
                </SignInButton>
            </LoginForm>
            <Footer>
                <Build>
                    Version {/*{dcaVersion}*/} (DCA {/*{dcaVersion}*/} build)
                </Build>
                <Legal>
                    <div>
                        Distribution and reproduction of this application is prohibited without a legal license agreement with Equisoft\UCT.
                    </div>
                    <div>
                        Violators will be prosecuted to the fullest extent of the law.
                    </div>
                </Legal>
            </Footer>
        </Wrapper>
    );
};

export default Login;
