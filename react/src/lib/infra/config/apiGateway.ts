export interface TypeOptions<T, U> {
    inType?: new () => T;
    outType?: new () => U;
}

export interface ApiGateway {
    setDisconnectHandler(onDisconnect: () => void): void;

    head(url: string): Promise<void>;

    get<T>(url: string, options?: TypeOptions<undefined, T>): Promise<T>;

    getArray<T>(url: string, options?: TypeOptions<undefined, T>): Promise<T[]>;

    getBlobData(url: string): Promise<any>;

    post<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U>;

    postReturnArray<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U[]>;

    put<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U>;

    delete(url: string): Promise<void>;
}