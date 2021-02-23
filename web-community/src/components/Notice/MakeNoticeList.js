import React, {useEffect, useState} from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";
import fileImage from "../../icon/file.svg";
import Loading from "../Loading";

export default function MakeNoticeList(props) {
    const [noticeData, setNoticeData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    let is_search = props.is_search;
    let search_type = props.search_type;
    let search_text = props.search_text;

    const url = (category, page) => {
        let url = "/notice"
        switch (category) {
            case 0:
                url += "/main";
                break;
            case 1:
                url += "/university";
                break;
            case 2:
                url += "/department";
                break;
            case 3:
                url += "/council";
                break;
        }
        if(is_search){
            switch (search_type) {
                case "select_title":
                    url += "/search/title?title="+search_text;
                    break;
                case "select_title_content":
                    url += "/search/title&content?title="+search_text+"&content="+search_text;
                    break;
                case "select_writer":
                    url += "/search/writer?writer="+search_text;
                    break;
            }
        }
        return url;
    }

    const categoryName = (category) => {
        switch (category) {
            case 0:
                return 0;
            case 1:
                return "university";
            case 2:
                return "department";
            case 3:
                return "council";
        }
    }

    const status = (status) =>{
        switch (status) {
            case "URGENT":
                return '긴급';
            case "TOP":
                return "상단";
            case "GENERAL":
                return "일반";
        }
    }

    const attachment = (file) =>{
        let attached = false;
        if(file != null){
            attached = true;
        }

        let style = {
            marginLeft: '5px',
            display: attached? "" : "none"
        }
        return style;
    }

    // <tr> 전체에 링크 연결
    let history = useHistory();
    const ToLink = (url) =>{
        history.push(url);
    }

    useEffect(() => {
        const fetchNoticeData = async () => {
            try {
                setError(null);
                setNoticeData(null);
                setLoading(true);
                const response = await axios.get(url(props.category));
                setNoticeData(response.data.data); // 데이터는 response.data 안에 있음
            } catch (e) {
                setError(e);
            }
            setLoading(false);
        };

        fetchNoticeData();
    }, [props.category, props.is_search]);

    if (loading) return <Loading/>;
    if (error) return <tr><td colSpan={5}>에러가 발생했습니다{error.toString()}</td></tr>;
    if (!noticeData) return null;
    if (Object.keys(noticeData).length==0) return <tr><td colSpan={5}>데이터가 없습니다.</td></tr>;
    return (
        <>
            {noticeData.map(data => (
                <tr key={data.notice_id}
                    onClick={()=>ToLink(`${props.match.url}/${categoryName(props.category) == 0 ? 
                        data.category.toLowerCase() : categoryName(props.category)}/${data.id}`)}>
                    <td>{status(data.status)}</td>
                    <td>
                            {data.title}
                            <img src={fileImage} style={attachment(data.attachment_file)}/>
                    </td>
                    <td>{data.created_by}</td>
                    <td>{data.created_at.substring(0,10)}</td>
                    <td>{data.views}</td>
                </tr>

            ))}
        </>
    );
}
