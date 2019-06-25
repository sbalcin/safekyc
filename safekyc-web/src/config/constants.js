
export const signUpUri = 'http://localhost:8080/auth/signUp/';
export const signInUri = 'http://localhost:8080/auth/signIn/'
export const retrieveQRCodeUri = 'http://localhost:8080/action/retrieveQRCode/'
export const shareQRCodeUri = 'http://localhost:8080/action/shareQRCode/'
export const verifyQRCodeUri = 'http://localhost:8080/action/verifyQRCode/'
export const retrieveSourceActionsUri = 'http://localhost:8080/action/retrieveSourceActions/'
export const retrieveTargetActionsUri = 'http://localhost:8080/action/retrieveTargetActions/'

/*export const signUpUri = 'http://37.59.110.96:8080/safekyc/auth/signUp/';
export const signInUri = 'http://37.59.110.96:8080/safekyc/auth/signIn/'
export const retrieveQRCodeUri = 'http://37.59.110.96:8080/safekyc/action/retrieveQRCode/'
export const shareQRCodeUri = 'http://37.59.110.96:8080/safekyc/action/shareQRCode/'
export const verifyQRCodeUri = 'http://37.59.110.96:8080/safekyc/action/verifyQRCode/'
export const retrieveSourceActionsUri = 'http://37.59.110.96:8080/safekyc/action/retrieveSourceActions/'
export const retrieveTargetActionsUri = 'http://37.59.110.96:8080/safekyc/action/retrieveTargetActions/'*/

export const generalRowLimit = 10;

export const currentUser = () => {
    let userKey = localStorage.getItem('userKey');
    let user = userKey ? userKey : undefined;
    return (
        user
    );
}