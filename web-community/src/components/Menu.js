import React from 'react';
import './Menu.css';
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Grid from "@material-ui/core/Grid";
import {BrowserRouter as Router, Switch, Route, Link, useRouteMatch} from "react-router-dom";

export default function Menu() {
    return (
        <div className="Menu">
            <Grid>
                <Row style={{borderBottom: 'solid 1px #d0d0d0', padding: '15px'}}>
                    <Col xs={3}>
                        <Link to="/">
                            <button className="Menu-logo">
                                가천대학교 AI&소프트웨어학부
                            </button>
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

                        <Link to="/jobInfo">
                            <button className="Menu-button">
                                채용정보
                            </button>
                        </Link>
                        <Link to="/contestInfo">
                            <button className="Menu-button">
                                공모전/대외활동
                            </button>
                        </Link>
                    </Col>
                    <Col xs={3}>
                        <Link to="/login">
                            <button className="Menu-button">
                                로그인
                            </button>
                        </Link>
                        <Link to="/join">
                            <button className="Menu-button blue-button">
                                회원가입
                            </button>
                        </Link>
                    </Col>
                </Row>
            </Grid>

        </div>
    );
}
