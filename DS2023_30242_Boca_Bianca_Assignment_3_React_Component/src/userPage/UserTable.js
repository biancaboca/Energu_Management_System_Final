import React from "react";
import Table from "../commons/tables/table";

const columns = [
    {
        Header: 'ID',
        accessor: 'id',
    },
    {
        Header: 'Address',
        accessor: 'address',
    },
    {
        Header: 'Description',
        accessor: 'description',
    },
    {
        Header: 'MaxHours',
        accessor: 'maxHours',
    },
 
];

const filters = [
    {
        accessor: 'Device',
    }
];

const backgroundPage = {
    background: 'rgba(255, 255, 255, 0.8)',
    padding: '20px',
    borderRadius: '8px',
    width: '800px',
    margin: '0 auto',
    marginTop: '50px',
    textAlign: 'center',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

class UserTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData
        };
    }

    render() {
        return (
            <Table
                data={this.state.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                style={backgroundPage} 
            />
        )
    }
}

export default UserTable;
