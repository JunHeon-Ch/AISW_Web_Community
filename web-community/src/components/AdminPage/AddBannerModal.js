import React, {forwardRef, useEffect, useState} from "react";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css"
import {ko} from "date-fns/esm/locale";
import styled from "styled-components";

function AddBannerModal(props) {
    const [imgBase64, setImgBase64] = useState(""); // 파일 base64
    const [imgFile, setImgFile] = useState(null);   //파일
    const [bannerInfo, setBannerInfo] = useState({banner_name: "", posting_period: "", banner_url: ""});   //배너
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    // datepicker 리셋
    const resetDate = () => {
        setStartDate(null);
        setEndDate(null);
    }

    const modalClose = () => {
        setImgBase64("");
        setImgFile(null);
        props.setShow(false);
        resetDate();
    }

    const handleChangeFile = (event) => {
        let reader = new FileReader();

        reader.onloadend = () => {
            // 2. 읽기가 완료되면 아래코드가 실행됩니다.
            const base64 = reader.result;
            if (base64) {
                setImgBase64(base64.toString()); // 파일 base64 상태 업데이트
            }
        }
        if (event.target.files[0]) {
            reader.readAsDataURL(event.target.files[0]); // 1. 파일을 읽어 버퍼에 저장합니다.
            setImgFile(event.target.files[0]); // 파일 상태 업데이트
        }
    }

    const handleInputChange = (event) => {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        if (name == "banner_image") {
            handleChangeFile(event);
        }
        setBannerInfo({
            ...bannerInfo,
            [name]: value
        });
        console.log(bannerInfo);
    }

    const handleSubmit = (event) => {
        let banner_info = {}
        sendData(banner_info);
        resetDate();
    }

    async function sendData(data) {
        await axios.post("/banner",
            {
                headers: {
                    "Content-Type": `application/json`
                },
                data,
            },
        ).then((res) => {
            console.log(res)
            alert("새 배너 등록완료!") // 실패 메시지
            // setModalShow(true)   // 완료 모달 띄우기
        }).catch(error => {
            let errorObject = JSON.parse(JSON.stringify(error));
            console.log("에러 발생 (새 사이트 등록)");
            console.log(errorObject);
            alert("새 배너 등록에 실패하였습니다."); // 실패 메시지
        })
    }

    const handleDatePicker = (dates) => {
        let [start, end] = dates;
        setStartDate(start);
        setEndDate(end);
        if (start != null && end != null) {
            start = start.getFullYear() + '-' + (start.getMonth() + 1) + '-' + start.getDate();
            end = end.getFullYear() + '-' + (end.getMonth() + 1) + '-' + end.getDate();

            // 기간 설정 posting_period
            
        }
    }

    return (
        <div className="AddBannerModal">
            <Modal show={props.show} onHide={modalClose}>
                <Modal.Header closeButton>
                    <Modal.Title>새 배너 추가</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>배너 명<span style={{color: "#FF0000"}}> *</span></Form.Label>
                            <Form.Control type="text" placeholder="" name="banner_name" onChange={handleInputChange}/>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>게시 기간<span
                                style={{color: "#FF0000", marginRight: "20px"}}> *</span></Form.Label>
                            <SelectDate
                                selected={startDate}
                                onChange={(date) => setStartDate(date)}
                                disabled
                                placeholderText="시작 날짜"
                                dateFormat="yyyy-MM-dd"
                            />
                            ~
                            <SelectDate
                                selected={endDate}
                                onChange={(date) => setEndDate(date)}
                                disabled
                                placeholderText="종료 날짜"
                                dateFormat="yyyy-MM-dd"
                            />
                            <DatePicker
                                selected={startDate}
                                onChange={handleDatePicker}
                                startDate={startDate}
                                endDate={endDate}
                                selectsRange
                                inline
                                locale={ko}
                                dateFormat="yyyy년 MM월 dd일"
                            />
                        </Form.Group>


                        <Form.Group className="mb-3">
                            <div><Form.Label>
                                배너 이미지 (500x500)<span style={{color: "#FF0000"}}> *</span>
                            </Form.Label></div>
                            {imgBase64 == "" ? null :
                                <div><img src={imgBase64} style={{width: "100%", objectFit: "cover"}} height={150}/>
                                </div>
                            }
                            <input type="file" id="imgFile" name="banner_image" accept='image/*'
                                   onChange={handleInputChange}/>
                            {/*<Form.Control type="file" size="sm" onChange={handleChangeFile}/>*/}
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={modalClose}>
                        닫기
                    </Button>
                    <Button variant="primary" type="submit" onClick={handleSubmit}>
                        추가
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default AddBannerModal;

const SelectDate = styled(DatePicker)`
  width: 80px;
  height: 22px;
  padding: 0;
  font-size: 14px;
  text-align: center;
  border: 0;
  background-color: white;
  outline: none;
  cursor: pointer;
`;
