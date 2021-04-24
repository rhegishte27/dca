import axios, { AxiosInstance } from 'axios';
import { ErrorMessage, notifyErrors } from '../../../components/general/error';
import { ApiGateway, TypeOptions } from './apiGateway';
import { JsonSerializer } from './jsonSerializer';
import { API_URL } from '../constants';

export class AxiosApiGateway implements ApiGateway {
    private axios: AxiosInstance | undefined;
    private onDisconnect: () => void;
    private interceptor?: number;

    constructor(private objectMapper: JsonSerializer) {
        this.onDisconnect = () => {};
    }

    setDisconnectHandler(onDisconnect: () => void): void {
        this.onDisconnect = onDisconnect;
    }

    getAxios(): AxiosInstance {
        if (!this.axios) {
            this.axios = axios.create({
                baseURL: API_URL,
                headers: {
                    'Content-Type': 'application/json',
                    Accept: 'application/json',
                },
                withCredentials: true,
            });
        }
        this.interceptor && this.axios.interceptors.response.eject(this.interceptor);
        this.interceptor = this.axios.interceptors.response.use(undefined, (error: any) =>
            this.handleLoginErrors(error),
        );
        return this.axios;
    }

    async head(url: string): Promise<void> {
        return this.getAxios().head(url);
    }

    async get<T>(url: string, options?: TypeOptions<undefined, T>): Promise<T> {
        return this.getAxios()
            .get<T>(url, {
                transformResponse: (data) => (options ? this.objectMapper.mapToObject(data, options.outType) : data),
            })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async getArray<T>(url: string, options?: TypeOptions<undefined, T>): Promise<T[]> {
        return this.getAxios()
            .get<T[]>(url, {
                transformResponse: (data) =>
                    options ? this.objectMapper.mapToArray(data, options.outType) : JSON.parse(data),
            })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async getBlobData(url: string): Promise<any> {
        return this.getAxios()
            .get(url, { responseType: 'blob' })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async post<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U> {
        return this.getAxios()
            .post<U>(url, data, {
                transformRequest: (d) => (options ? this.objectMapper.mapToJson(d, options.inType) : d),
                transformResponse: (d) => (options ? this.objectMapper.mapToObject(d, options.outType) : d),
            })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async postReturnArray<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U[]> {
        return this.getAxios()
            .post<U[]>(url, data, {
                transformRequest: (d) => (options ? this.objectMapper.mapToJson(d, options.inType) : d),
                transformResponse: (d) => (options ? this.objectMapper.mapToArray(d, options.outType) : d),
            })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async put<T, U>(url: string, data?: any, options?: TypeOptions<T, U>): Promise<U> {
        return this.getAxios()
            .put<U>(url, data, {
                transformRequest: (d) => (options ? this.objectMapper.mapToJson(d, options.inType) : d),
                transformResponse: (d) => (options ? this.objectMapper.mapToObject(d, options.outType) : d),
            })
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    async delete(url: string): Promise<void> {
        return this.getAxios()
            .delete(url)
            .then((response) => response.data)
            .catch((err) => this.processErrorBody(err));
    }

    private processErrorBody = async (err: any): Promise<any> => {
        let body: ErrorMessage[] = [];
        if (err.response) {
            if (err.response.request && err.response.request.response) {
                const errResponse = err.response.request.response;
                const errObject = JSON.parse(errResponse);
                body =
                    errObject instanceof Array
                        ? errObject.map((e: any) => ({
                              message: e.message,
                              extraInformation: e.extraInformation,
                          }))
                        : [
                              {
                                  message: errObject.message,
                                  extraInformation: errObject.extraInformation,
                              },
                          ];
                err.body = body;
            }
        } else if (err.isAxiosError) {
            body = [{ message: err.message, extraInformation: 'Unable to contact the API' }];
            err.body = body;
        } else {
            body = [{ message: err.message, extraInformation: err.stack }];
            err.body = body;
        }
        notifyErrors(body);
        return Promise.reject(body);
    };

    private async handleLoginErrors(err: any): Promise<any> {
        if (err.config && err.response && err.response.status === 401) {
            // auth cookie has been thrashed, retry request once
            if (!err.config.__isRetry) {
                err.config.__isRetry = true;
                return axios.request(err.config);
            }
            err.config.__isRetry = false;
            this.onDisconnect();
        }
        return Promise.reject(err);
    }
}
