
/*export const signUpUri = 'http://localhost:8080/auth/signUp/';
export const signInUri = 'http://localhost:8080/auth/signIn/'
export const retrieveQRCodeUri = 'http://localhost:8080/action/retrieveQRCode/'
export const shareQRCodeUri = 'http://localhost:8080/action/shareQRCode/'
export const verifyQRCodeUri = 'http://localhost:8080/action/verifyQRCode/'
export const retrieveSourceActionsUri = 'http://localhost:8080/action/retrieveSourceActions/'
export const retrieveTargetActionsUri = 'http://localhost:8080/action/retrieveTargetActions/'*/

export const signUpUri = 'https://polytopesoft.com:8443/safekyc/auth/signUp/';
export const signInUri = 'https://polytopesoft.com:8443/safekyc/auth/signIn/'
export const retrieveQRCodeUri = 'https://polytopesoft.com:8443/safekyc/action/retrieveQRCode/'
export const shareQRCodeUri = 'https://polytopesoft.com:8443/safekyc/action/shareQRCode/'
export const verifyQRCodeUri = 'https://polytopesoft.com:8443/safekyc/action/verifyQRCode/'
export const retrieveSourceActionsUri = 'https://polytopesoft.com:8443/safekyc/action/retrieveSourceActions/'
export const retrieveTargetActionsUri = 'https://polytopesoft.com:8443/safekyc/action/retrieveTargetActions/'

export const generalRowLimit = 10;

export const currentUser = () => {
    let userKey = localStorage.getItem('userKey');
    let user = userKey ? userKey : undefined;
    return (
        user
    );
}