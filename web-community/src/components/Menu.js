import React, {useEffect, useState} from 'react';
import './Menu.css';
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Grid from "@material-ui/core/Grid";
import {Link, useHistory} from "react-router-dom";
import logo from "../image/logo3.png";
import {useDispatch, useSelector} from "react-redux";
import MyPage from "./MyPage";
import Button from "react-bootstrap/Button";
import GoogleLogin from "react-google-login";
import googleLogo from '../image/google-logo.png';
import {GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL} from '../constants';

export default function Menu() {
    const history = useHistory();

    // redux toolkit
    const user = useSelector(state => state.user)
    const dispatch = useDispatch()

    const [modalShow, setModalShow] = useState(false);

    useEffect(() => {
        console.log(user);
    }, [])

    // 이미 있는 회원인지 확인
    const isExistUser = () => {

    }

    // 구글 연동 성공시
    const handleLoginSuccess = (result) => {
        console.log("로그인 성공", result)

        history.push('/')
    }

    // 구글 연동 실패시
    const handleLoginFailure = (result) => {
        console.log("로그인 실패", result)
    }

    // 구글 연동 성공시
    const handleJoinSuccess = (result) => {
        console.log("회원가입 성공", result)

        history.push({pathname: '/join', state: {google_data: result}})
    }

    // 구글 연동 실패시
    const handleJoinFailure = (result) => {
        console.log("회원가입 실패", result)
    }

    return (
        <div className="Menu">
            <Grid>
                <Row style={{borderBottom: 'solid 1px #d0d0d0', padding: '15px'}}>
                    <Col xs={3}>
                        <Link to="/">
                            <img src={logo} style={{width: "120px"}}/>
                        </Link>
                    </Col>
                    <Col xs={6}>
                        <Link to="/notice">
                            <button className="Menu-button">
                                공지사항
                            </button>
                        </Link>
                        <Link to="/board">
                            <button className="Menu-button">
                                게시판
                            </button>
                        </Link>
                        <Link to="/deptInfo">
                            <button className="Menu-button">
                                학과정보
                            </button>
                        </Link>

                        {/*<Link to="/jobInfo">*/}
                        {/*    <button className="Menu-button">*/}
                        {/*        채용정보*/}
                        {/*    </button>*/}
                        {/*</Link>*/}
                        <Link to="/contestInfo">
                            <button className="Menu-button">
                                공모전/대외활동
                            </button>
                        </Link>
                        <Link to="/goodInfo">
                            <button className="Menu-button">
                                유용한사이트
                            </button>
                        </Link>
                    </Col>

                    <Col xs={3}>
                        <button className="Menu-button" onClick={() => setModalShow(true)}>
                            {user.userData.name}
                        </button>
                        <Link to="/manager">
                            <button className="Menu-button">
                                관리자페이지
                            </button>
                        </Link>
                    </Col>

                    <MyPage show={modalShow} onHide={() => setModalShow(false)}/>

                    {/*{*/}
                    {/*    (user.isOnline && user.userData != null) ? */}
                    {/*        (*/}
                    {/*        <>*/}
                    {/*            <Col xs={3}>*/}
                    {/*                <button className="Menu-button" onClick={() => setModalShow(true)}>*/}
                    {/*                    {user.userData.name}*/}
                    {/*                </button>*/}
                    {/*                <Link to="/manager">*/}
                    {/*                    <button className="Menu-button">*/}
                    {/*                        관리자페이지*/}
                    {/*                    </button>*/}
                    {/*                </Link>*/}
                    {/*            </Col>*/}

                    {/*            <MyPage show={modalShow} onHide={() => setModalShow(false)}/>*/}

                    {/*        </>*/}
                    {/*    ) : (*/}
                    {/*        <Col xs={3}>*/}
                                {/*<GoogleLogin*/}
                                {/*    clientId='1051028847648-3edseaslg7hqbrgo5q2thhdag9k6q10e.apps.googleusercontent.com'*/}
                                {/*    render={renderProps => (*/}
                                {/*        <button className="Menu-button" onClick={renderProps.onClick}*/}
                                {/*                disabled={renderProps.disabled}>로그인</button>*/}
                                {/*    )}*/}
                                {/*    onSuccess={result => {handleLoginSuccess(result)}}*/}
                                {/*    onFailure={result => {handleLoginFailure(result)}}*/}
                                {/*    // uxMode='redirect'*/}
                                {/*    // redirectUri="http://localhost:3000/user/signup"*/}
                                {/*    cookiePolicy={'single_host_origin'}*/}
                                {/*/>*/}

                                {/*<GoogleLogin*/}
                                {/*    clientId='1051028847648-3edseaslg7hqbrgo5q2thhdag9k6q10e.apps.googleusercontent.com'*/}
                                {/*    render={renderProps => (*/}
                                {/*        <button className="Menu-button blue-button" onClick={renderProps.onClick}*/}
                                {/*                disabled={renderProps.disabled}>회원가입</button>*/}
                                {/*    )}*/}
                                {/*    onSuccess={result => {handleJoinSuccess(result)}}*/}
                                {/*    onFailure={result => {handleJoinFailure(result)}}*/}
                                {/*    // uxMode='redirect'*/}
                                {/*    // redirectUri="http://localhost:3000/user/signup"*/}
                                {/*    cookiePolicy={'single_host_origin'}*/}
                                {/*/>*/}
                    {/*            <div className="social-signup">*/}
                    {/*                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>*/}
                    {/*                    <img src={googleLogo} width="10px" alt="Google"/> Sign up with Google</a>*/}
                    {/*            </div>*/}
                    {/*        </Col>*/}
                    {/*    )*/}
                    {/*}*/}
                </Row>
            </Grid>

        </div>
    );
}
